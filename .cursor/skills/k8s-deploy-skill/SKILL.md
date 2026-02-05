# Kubernetes Deployment Skill

Deploy the Todo Application to a Kubernetes cluster.

## Overview

This skill provides Kubernetes manifests and deployment scripts for the Todo application, deploying both frontend and backend services in a dedicated namespace.

## Architecture

- **Namespace**: `todo-app` - Isolated namespace for all application resources
- **Backend**: Spring Boot application (2 replicas) with ClusterIP service for internal communication
- **Frontend**: React/Nginx application (2 replicas) with NodePort service for external access

## Files

| File | Description |
|------|-------------|
| `scripts/namespace.yaml` | Namespace definition |
| `scripts/configmap.yaml` | ConfigMaps for backend and frontend |
| `scripts/backend-deployment.yaml` | Backend Deployment + ClusterIP Service |
| `scripts/frontend-deployment.yaml` | Frontend Deployment + NodePort Service |
| `scripts/todo-app-all.yaml` | Combined manifest (all resources) |
| `scripts/deploy.sh` | Automated deployment script |

## Prerequisites

1. Kubernetes cluster (minikube, kind, or production cluster)
2. `kubectl` configured with cluster access
3. Docker images built and available:
   - `todo-backend:latest`
   - `todo-frontend:latest`

## Deployment

### Option 1: Using the deployment script

```bash
./scripts/deploy.sh
```

### Option 2: Manual deployment

```bash
# Apply all resources at once
kubectl apply -f scripts/todo-app-all.yaml

# Or apply individually
kubectl apply -f scripts/namespace.yaml
kubectl apply -f scripts/configmap.yaml
kubectl apply -f scripts/backend-deployment.yaml
kubectl apply -f scripts/frontend-deployment.yaml
```

## Accessing the Application

The frontend is exposed via NodePort on port **30080**:

```bash
# For minikube
minikube ip  # Get the IP, then access http://<ip>:30080

# For other clusters
kubectl get nodes -o wide  # Get node IP, then access http://<node-ip>:30080
```

## Useful Commands

```bash
# Check deployment status
kubectl get all -n todo-app

# View logs
kubectl logs -f deployment/todo-backend -n todo-app
kubectl logs -f deployment/todo-frontend -n todo-app

# Scale deployments
kubectl scale deployment/todo-backend --replicas=3 -n todo-app

# Delete all resources
kubectl delete namespace todo-app
```

## Service Communication

- **Frontend → Backend**: `http://todo-backend-service:8080` (ClusterIP)
- **External → Frontend**: `http://<node-ip>:30080` (NodePort)
