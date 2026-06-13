param(
    [string]$Url,
    [int]$ProductId,
    [int]$Quantity,
    [int]$Requests
)

if (-not $Requests) {
    Write-Host "Usage: .\concurrent-test.ps1 <url> <product_id> <quantity> <number_of_requests>"
    Write-Host "Example: .\concurrent-test.ps1 http://localhost:8080/api/orders/pessimistic 1 1 5"
    exit
}

Write-Host "Starting $Requests concurrent requests to $Url"

$jobs = @()
for ($i = 1; $i -le $Requests; $i++) {
    $body = '{"productId": ' + $ProductId + ', "quantity": ' + $Quantity + ', "userId": ' + $i + '}'
    
    $job = Start-Job -ScriptBlock {
        param($url, $body)
        try {
            $response = Invoke-RestMethod -Uri $url -Method POST -Headers @{"Content-Type"="application/json"} -Body $body
            return $response
        } catch {
            return $_.Exception.Message
        }
    } -ArgumentList $Url, $body
    
    $jobs += $job
}

Wait-Job -Job $jobs | Out-Null
$results = Receive-Job -Job $jobs
Remove-Job -Job $jobs

foreach ($result in $results) {
    Write-Host $result
}

Write-Host "`nAll requests completed."
