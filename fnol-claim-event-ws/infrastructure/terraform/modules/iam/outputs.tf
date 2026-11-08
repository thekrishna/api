output "application_role_name" {
  description = "Name of the FNOL application IAM role."
  value       = aws_iam_role.application.name
}

output "application_role_arn" {
  description = "ARN of the FNOL application IAM role."
  value       = aws_iam_role.application.arn
}
