# Bot Detection System
This application continuously analyzes a remote access log file to detect bot activity based on a set of simple rules.


## Implementation
The implementation is structured over three main components:
* **Downloader**: continuously download (with a fixed delay -> sequentially) from this [URL](http://www.almhuette-raith.at/apache-log/access.log). Uses a resumable download approach to only get the latest non downloaded bytes at each task execution.
* **Reader**: scheduled async task (with a fixed rate -> in parallel) that reads a log line and publishes it in a RabbitMQ queue.
* **Analyzer**: subscribes to the queue and performs the analysis of incoming requests.

State (log file cursor, recorded hits, and analyzed logs) is maintained in an embedded H2 database.
While the downloader is sequential, the reader/analyzer components are designed to run in parallel to be as scalable as possible. 

### Run instructions
There is a docker compose file that composes the app, and the broker services. Simply run `docker-compose up` at the root of the project.

DB console is accessible via http://localhost:8080/h2-console (user=sa/password=)

RabbitMq console is accessible via http://localhost:15672 (user=guest/password=guest)

There is an initial delay of 3 min before the processing starts.

### Results & Limitations
With a reading fixed rate of 10ms, and a scheduler thread pool size of 100, the system should be able to handle **100 analysis per second**.
However, the constraint of maintaining the log read cursor via a DB pessimistic lock affects the performance, and it gets stuck at around **30 analysis per second**
(the read tasks need to wait for the cursor lock to be released in order to perform their own select for update).
This can assessed by running the system and watching the broker pub/sub rate and the analysis data on the DB.
I have made multiple runs of 30 minutes, while observing the pub/sub rate, and the DB cursor position. 
Results consistently show a total of 51000 log lines analyzed, and a pub/sub rate decreasing from 100 to around 15 msg/s.

**Edit:** Introduced bulk read/analyze of data (publish RabbitMQ message with chunk size of a 100 log lines, and analyse and insert chunk size of log lines in bulk).
This improved the performance significantly to around **485 analysis per second** (873500 lines analyzed in 0.5h).
This was recorded while running the app in Docker on my laptop and watching the value of the file read cursor in the `LOG_FILE_CURSOR` table (Cf. http://localhost:8080/h2-console)

