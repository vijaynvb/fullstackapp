#!/bin/bash
set -e

echo "=== Containerizing Todo Application ==="
echo ""

# Navigate to project root
cd "$(dirname "$0")/../../.."

# Build and start containers
echo "Building and starting containers..."
docker compose up --build -d

echo ""
echo "=== Containers Started Successfully ==="
echo ""
echo "Services:"
echo "  - Frontend: http://localhost:3000"
echo "  - Backend:  http://localhost:8080"
echo "  - API Docs: http://localhost:8080/swagger-ui.html"
echo ""
echo "Useful commands:"
echo "  - View logs:     docker compose logs -f"
echo "  - Stop:          docker compose down"
echo "  - Rebuild:       docker compose up --build -d"
echo ""
