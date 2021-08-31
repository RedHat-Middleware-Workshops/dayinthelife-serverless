# Run Locally using tutorial web app

Set the following vars to connect to an openshift cluster:

```
export DOMAIN=<your-ocp-domain>
export APPS_DOMAIN=apps.$DOMAIN
export OPENSHIFT_OAUTHCLIENT_ID=tutorial-web-app 
export OPENSHIFT_OAUTH_HOST=oauth-openshift.$APPS_DOMAIN 
export OPENSHIFT_API=api.$DOMAIN:6443 
export OPENSHIFT_HOST=console-openshift-console.$APPS_DOMAIN 
export OPENSHIFT_VERSION='4' 
export INSTALLED_SERVICES="{'3scale': {'Host': 'https://3scale-admin.${APPS_DOMAIN}','Version': '2.6'}}" 
export WALKTHROUGH_LOCATIONS=<your-path>
```

Run locally:

```
docker run --rm -it --name solex -p 5001:5001 -v $PWD/docs/labs:/opt/user-walkthroughs -e NODE_ENV=production -e THREESCALE_WILDCARD_DOMAIN=local.localdomain -e OPENSHIFT_VERSION=4 -e WALKTHROUGH_LOCATIONS=/opt/user-walkthroughs quay.io/redhatintegration/tutorial-web-app:2.28.1-workshop
```