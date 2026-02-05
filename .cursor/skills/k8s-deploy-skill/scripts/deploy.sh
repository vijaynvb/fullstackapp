#!/bin/bash
# =============================================================================
# Todo Application - Kubernetes Deployment Script
# =============================================================================

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
NAMESPACE="todo-app"

echo "=========================================="
echo "Deploying Todo Application to Kubernetes"
echo "=========================================="

# Check if kubectl is available
if ! command -v kubectl &> /dev/null; then
    echo "Error: kubectl is not installed or not in PATH"
    exit 1
fi

# Check cluster connection
echo "Checking cluster connection..."
if ! kubectl cluster-info &> /dev/null; then
    echo "Error: Cannot connect to Kubernetes cluster"
    exit 1
fi

echo "Cluster connection verified."

# Deploy using the combined manifest
echo ""
echo "Deploying all resources..."
kubectl apply -f "${SCRIPT_DIR}/todo-app-all.yaml"

# Wait for deployments to be ready
echo ""
echo "Waiting for backend deployment..."
kubectl rollout status deployment/todo-backend -n "${NAMESPACE}" --timeout=120s

echo ""
echo "Waiting for frontend deployment..."
kubectl rollout status deployment/todo-frontend -n "${NAMESPACE}" --timeout=120s

# Display deployment status
echo ""
echo "=========================================="
echo "Deployment Complete!"
echo "=========================================="
echo ""
echo "Resources in namespace '${NAMESPACE}':"
kubectl get all -n "${NAMESPACE}"

echo ""
echo "=========================================="
echo "Access Information"
echo "=========================================="
echo "Frontend is accessible via NodePort: 30080"
echo "Use: http://<node-ip>:30080"
echo ""
echo "To get node IP (minikube):"
echo "  minikube ip"
echo ""
echo "To get node IP (kind/other):"
echo "  kubectl get nodes -o wide"
echo "=========================================="
