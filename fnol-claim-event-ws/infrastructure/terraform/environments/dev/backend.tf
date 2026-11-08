terraform {
  backend "s3" {
    bucket         = "fnol-tfstate-dev"
    key            = "fnol/dev/terraform.tfstate"
    region         = "us-east-1"
    encrypt        = true
    dynamodb_table = "fnol-tfstate-locks-dev"
  }
}
