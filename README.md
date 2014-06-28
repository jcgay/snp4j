#SNP4J

A blocking implementation of the SNP protocol to talk with [Snarl](http://snarl.fullphat.net/).

##Usage
1. Create the destination,
2. Define an application,
2. Initialize the notifier,
3. Create a notification,
4. Send the notification,
5. Release resources.

```	
	Server destination = Server.builder()
                .withHost("localhost")
                .withPort(9887)
                .build();
	Application application = Application.of("application/x-vnd-apache.maven", "Maven");
	Notifier notifier = SnpNotifier.of(application, destination);
	
	Notification notification = new Notification();
	notification.setText("Hello World !");
	notification.setTitle("First notification");
	
	try {
    	notifier.send(notification);
    } finally {
        Closeables.closeQuietly(notifier);
    }
```

The application is automatically registered against Snarl, and unregistered when closing resource.

##Installation

#### Add dependency with your favorite build tool.

Example with *Maven*:

    <dependencies>
        <dependency>
            <groupId>com.github.jcgay.snp4j</groupId>
            <artifactId>snp4j</artifactId>
            <version>0.2</version>
        </dependency>
    </dependencies>
