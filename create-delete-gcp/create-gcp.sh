#!/bin/bash

set -e

########################################
# Configuration
########################################

PROJECT_ID="one-step-gcp"

# GKE
CLUSTER_NAME="one-step-cluster"
ZONE="us-central1-a"
REGION="us-central1"

# Artifact Registry
AR_REGION="asia-south1"
AR_REPOSITORY="one-step-repo"

# Storage
BUCKET_NAME="one-step-storage"

# Kubernetes
NAMESPACE="uat1"

########################################

echo "Setting project..."

gcloud config set project $PROJECT_ID

########################################
# Create Storage Bucket
########################################

echo "Creating Storage Bucket..."

gcloud storage buckets create gs://$BUCKET_NAME \
    --project=$PROJECT_ID \
    --location=$REGION \
    --uniform-bucket-level-access

########################################
# Create Artifact Registry
########################################

echo "Creating Artifact Registry..."

gcloud artifacts repositories create $AR_REPOSITORY \
    --repository-format=docker \
    --location=$AR_REGION \
    --description="Docker repository"

########################################
# Create GKE Cluster
########################################

echo "Creating GKE Cluster..."

gcloud container clusters create $CLUSTER_NAME \
    --zone=$ZONE \
    --machine-type=e2-medium \
    --num-nodes=1 \
    --disk-size=20 \
    --disk-type=pd-standard

########################################
# Configure kubectl
########################################

echo "Getting cluster credentials..."

gcloud container clusters get-credentials \
    $CLUSTER_NAME \
    --zone=$ZONE

########################################
# Create Namespace
########################################

echo "Creating namespace..."

kubectl create namespace $NAMESPACE

########################################

echo ""
echo "======================================="
echo "Environment created successfully"
echo "======================================="

echo "Project        : $PROJECT_ID"
echo "Cluster        : $CLUSTER_NAME"
echo "Zone           : $ZONE"
echo "Bucket         : $BUCKET_NAME"
echo "Repository     : $AR_REPOSITORY"
echo "Namespace      : $NAMESPACE"
