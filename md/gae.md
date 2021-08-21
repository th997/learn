# GCE

## intall -- https://cloud.google.com/sdk/docs/?hl=zh-cn
wget https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-sdk-218.0.0-darwin-x86_64.tar.gz?hl=zh-cn

tar -zxvf ..

./google-cloud-sdk/install.sh

./google-cloud-sdk/bin/gcloud init


## dev app -- https://cloud.google.com/appengine/docs/flexible/go/quickstart?hl=zh-cn
go get -u -d github.com/GoogleCloudPlatform/golang-samples/appengine_flexible/helloworld

cd $GOPATH/src/github.com/GoogleCloudPlatform/golang-samples/appengine_flexible/helloworld

go run *.go

gcloud app deploy


## app.yaml
https://cloud.google.com/appengine/docs/flexible/go/reference/app-yaml#resource-settings


