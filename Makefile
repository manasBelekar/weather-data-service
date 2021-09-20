# -----------------------------------------------------------------------------
# Define Commands
# -----------------------------------------------------------------------------

build: # Build all components as production ready assets
	@docker build -t project/test:latest .
	@helm dep update ./helm/project

deploy: # Deploy all assets to the kubernetes cluster
	@kubectl apply -k ./config/local/
	@helm upgrade --install -n $(shell make deploy/namespace) -f ./config/local/values.yaml $(USER)-project helm/project
	
deploy/namespace: # Runs the solution locally using pm2
    @cat ./config/local/kustomization.yaml | yq e '.namespace' -	
