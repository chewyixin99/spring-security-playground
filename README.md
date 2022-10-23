### Playground to implement spring security

- Client app

  - `$ cd demo`
  - `$ mvn spring-boot:run`

- Authorization server app

  - `$ cd oauth-authorization-server`
  - `$ mvn spring-boot:run`

- Resource server app

  - `$ cd oauth-resource-server`
  - `$ mvn spring-boot:run`

### To edit host files to enable connection to 'auth-server'

- https://phoenixnap.com/kb/how-to-edit-hosts-file-in-windows-mac-or-linux#ftoc-heading-8
- add `127.0.0.1 auth-server` under localhost


#### Credits
- https://www.baeldung.com/spring-security-oauth-auth-server
- https://www.youtube.com/watch?v=tWcqSIQr6Ks&ab_channel=DailyCodeBuffer
