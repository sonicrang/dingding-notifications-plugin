**Modify for send markdown message with pipeline**

**Build in docker:**

```shell
docker pull maven:3.3.9

docker run -it --rm --name my-maven-project -v [PROJECT PATH]:/usr/src/mymaven -w /usr/src/mymaven maven:3.3.9 mvn package
```

**Install Plugin:**

find hpi file in [PROJECT PATH]/target/dingding-notifications.hpi and upload it in Jenkins Plugin Management Page

**How to Use：**

```shell
dingTalk accessToken:"xxxxx",text:"@13811111111 \n ####this is title \n\n this is content",atMobiles:"13811111111"
```




