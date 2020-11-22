
kubectl create secret generic tls-secret --from-file=tls.key=./privkey.pem --from-file=tls.crt=./fullchain.pem
kubectl create secret generic tls-secret --from-file=tls.key=./privkey.pem --from-file=tls.crt=./fullchain.pem -n kubernetes-dashboard

