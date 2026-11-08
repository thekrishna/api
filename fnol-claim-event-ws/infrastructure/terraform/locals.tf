locals {
  normalized_environment = lower(trimspace(var.environment))
  resource_prefix        = "${lower(var.project_name)}-${local.normalized_environment}"

  default_tags = merge(
    {
      Project     = var.project_name
      Environment = local.normalized_environment
      ManagedBy   = "terraform"
      Service     = "fnol"
    },
    var.tags
  )
}
