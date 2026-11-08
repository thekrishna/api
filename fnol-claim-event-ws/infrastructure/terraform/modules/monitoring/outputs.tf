output "log_group_name" {
  description = "Application log group name."
  value       = aws_cloudwatch_log_group.application.name
}
