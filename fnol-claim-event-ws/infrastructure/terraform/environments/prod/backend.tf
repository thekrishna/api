terraform {
  backend "s3" {
    bucket         = "fnol-tfstate-prod"
    key            = "fnol/prod/terraform.tfstate"
    region         = "us-east-1"
    encrypt        = true
    dynamodb_table = "fnol-tfstate-locks-prod"
  }
}
