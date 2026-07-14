#!/bin/bash

set -e

########################################
# Configuration
########################################

PROJECT_ID="one-step-gcp"

# GKE
CLUSTER_NAME="one-step-cluster"
ZONE="us-central1-a"

# Artifact Registry
AR_REGION="asia-south1"
AR_REPOSITORY="one-step-repo"

# Storage
BUCKET_NAME="one-step-bucket"

# Kubernetes
NAMESPACE="uat1"

########################################

echo "Setting project..."

gcloud config set project $PROJECT_ID

echo "========================================"
echo "Deleting Kubernetes resources..."
echo "========================================"

kubectl delete deployment spring-dsl-flow \
    -n $NAMESPACE \
    --ignore-not-found

kubectl delete service spring-dsl-flow \
    -n $NAMESPACE \
    --ignore-not-found

kubectl delete namespace $NAMESPACE \
    --ignore-not-found

echo "========================================"
echo "Deleting GKE Cluster..."
echo "========================================"

gcloud container clusters delete \
    $CLUSTER_NAME \
    --zone=$ZONE \
    --quiet

echo "========================================"
echo "Deleting Cloud Storage Bucket..."
echo "========================================"

gcloud storage rm -r gs://$BUCKET_NAME

echo "========================================"
echo "Deleting Artifact Registry..."
echo "========================================"

gcloud artifacts repositories delete \
    $AR_REPOSITORY \
    --location=$AR_REGION \
    --quiet

echo ""
echo "========================================"
echo "Cleanup Completed Successfully"
echo "========================================"

# ₹28,230.87 