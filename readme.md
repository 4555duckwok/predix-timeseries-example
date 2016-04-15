Before running this example predix services should be set up. 

Example of services setup below is described for windows based dev environment with cf and uaa cli installed. The only windows specific command is `findstr`, which is analog to unix command `grep`.

Services setup example:
1. Log into cloud foundry
    * `cf login`
1. Create UAA instance if you have not yet:
    * `cf cs predix-uaa Tiered demo-uaa -c "{\"adminClientSecret\":\"demo@pass\"}"`
1. Grab UAA instance guid
    * `cf curl /v2/service_instances?q=name:demo-uaa | findstr /R \"guid\"`
1. Create Timeseries service
    * `cf cs predix-timeseries Bronze demo-ts -c "{\"trustedIssuerIds\":[\"https://74dbb0f8-fcbd-4434-9c59-eb2022bbd02c.predix-uaa.run.aws-usw02-pr.ice.predix.io/oauth/token\"]}"`
1. Grab timeseries instance guid
    * `cf curl /v2/service_instances?q=name:demo-ts | findstr /R \"guid\"`
1. Connect uaac to new UAA instance
    * `uaac target https://74dbb0f8-fcbd-4434-9c59-eb2022bbd02c.predix-uaa.run.aws-usw02-pr.ice.predix.io`
1. Log into uaa
    * `uaac token client get admin -s demo@pass`
1. Create client
    * `uaac client add demo-ts-client-id --secret client@pass --authorized_grant_types "authorization_code,client_credentials,refresh_token,password" --authorities "openid,uaa.none,uaa.resource" --scope "uaa.none,openid" --autoapprove openid`
1. Create user
    * `uaac user add demo-ts-user-id --emails demo-ts-user@mail --password user@pass`
1. Add client authorities for accessing timeseries instance
    * `uaac client update demo-ts-client-id --authorities "openid,uaa.none,uaa.resource,timeseries.zones.a50c2e0a-ec09-4d7e-8551-64a31deddc48.user,timeseries.zones.a50c2e0a-ec09-4d7e-8551-64a31deddc48.ingest,timeseries.zones.a50c2e0a-ec09-4d7e-8551-64a31deddc48.query"`


Example can be run by
```
mvn spring-boot:run
```