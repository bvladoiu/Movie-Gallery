# Movie-Gallery

 A kotlin movie gallery app

### Dependencies

Glide - for loading imagess. Has safe values for and allows control over the image cache.   

[Fuel](https://www.baeldung.com/kotlin/fuel) HTTP for Kotlin - in the author’s words, the easiest HTTP networking library for Kotlin/Android

GreenRobot [EventBus](https://greenrobot.org/eventbus/) - the most lightweight and flexible way to signal the ui the result of long running opperations.

The application Uses a JobIntentService to perform network requests off of the ui thread and the eventBus to signal back. A data cache can be integrated starightforward. The http service would cache the data after any successfull operation and end in a broadcast/event over the eventbus. Any component on the UI that may be interested can react on the event. In a later iteration the in memory cache can be replaced by a persisten cache/ database without renouncing the architecture so far.
