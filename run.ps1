Write-Host "Starting VoteSecure Application..." -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Please ensure no other service is running on port 8080" -ForegroundColor Yellow

# Use maven wrapper if it exists, otherwise fallback to mvn
if (Test-Path "mvnw.cmd") {
    Write-Host "Using Maven Wrapper..." -ForegroundColor Green
    .\mvnw.cmd spring-boot:run
} else {
    Write-Host "Using System Maven..." -ForegroundColor Green
    mvn spring-boot:run
}
