terraform {
  backend "s3" {
    bucket         = "fnol-tfstate-test"
    key            = "fnol/test/terraform.tfstate"
    region         = "us-east-1"
    encrypt        = true
    dynamodb_table = "fnol-tfstate-locks-test"
  }
}
