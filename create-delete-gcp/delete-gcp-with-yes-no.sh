#!/bin/bash

set -e

# ========================================
# Configuration
# ========================================

PROJECT_ID="one-step-gcp"

# GKE
CLUSTER_NAME="one-step-cluster"
ZONE="us-central1-a"

# Artifact Registry
AR_REGION="asia-south1"
AR_REPOSITORY="one-step-repo"

# Cloud Storage
BUCKET_NAME="one-step-bucket"

# Kubernetes
NAMESPACE="uat1"

# ========================================

confirm() {
    read -p "$1 (y/N): " ans
    [[ "$ans" == "y" || "$ans" == "Y" ]]
}

echo "Using project: $PROJECT_ID"

gcloud config set project $PROJECT_ID

echo "========================================"

#
# Delete Kubernetes resources
#
if confirm "Delete Kubernetes Deployment?"; then
    kubectl delete deployment spring-dsl-flow -n $NAMESPACE --ignore-not-found
fi

if confirm "Delete Kubernetes Service?"; then
    kubectl delete service spring-dsl-flow -n $NAMESPACE --ignore-not-found
fi

if confirm "Delete Namespace?"; then
    kubectl delete namespace $NAMESPACE --ignore-not-found
fi

#
# Delete GKE Cluster
#
if confirm "Delete GKE Cluster?"; then
    gcloud container clusters delete \
        $CLUSTER_NAME \
        --zone=$ZONE \
        --quiet
fi

#
# Delete Cloud Storage bucket
#

if confirm "Delete Storage Bucket?"; then
    gcloud storage rm -r gs://$BUCKET_NAME
fi

#
# Delete Artifact Registry Repository
#
if confirm "Delete Artifact Registry Repository?"; then
    gcloud artifacts repositories delete \
        $AR_REPOSITORY \
        --location=$AR_REGION \
        --quiet
fi

echo
echo "Cleanup completed."



# List clusters
gcloud container clusters list

# List buckets
gcloud storage buckets list

# List Artifact Registry repositories
gcloud artifacts repositories list

# List deployments
kubectl get deployments -A