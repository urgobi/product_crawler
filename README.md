# product_crawler
Product Crawler and Spec Downloader

Usage:
Application -p <ProductFileWithProductUrlSeparatedByLine> -o <OutputFolder> -d <true/false for downloading file> -u <user> -n <name> -z <zip>

-p Products should be provided with line delimited in the product file
-o Output Folder where the spec should be downloaded
-d true/false for downloading the file

Application user JDK8 and above for running

Application uses Spring Boot initial idea was to run as a standalone web application and as such spring boot jars are not necessary 
as it is using only HttpClient and File IO 

