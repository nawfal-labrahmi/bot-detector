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
 
 
## Project management & Architecture
Now, you need to make such a system "real-time" and ready for production, to support:
* 200 requests per second in one month,
* 2000 in three months,
* 20000 in 6 months,
* 1000000 in 2 - 3 years.

1. _Do you think it is possible? Why?_
Theoretically, yes, it possible. Ideally, linear scalability can be attained. However, it's rare concretely speaking, as it depends highly on the processing.
In our case, depending on the quality of the processing (and their number) and the data streaming, I would say it's an ambitious goal, but it's hard to say without regular testing and assessment.
Generally speaking, it's easier to scale linearly when the workers are independent of each other. That being the case or not depends primarily on the processing.

2. _How will you architecture it (name its components)? Based on which technologies?_
I am fairly new to this field, but based on my recent readings, here's how I would answer:
    - Layer of collecting and streaming unstructured data (Kafka topics - durable, partitioned data queues)
    - Layer of transformation and storing of structured data (Hadoop HDFS - high performance distributed storage)
    - Realtime analytics and ML, the business layer per se. (Spark streaming - high performance streaming)
    - Offline batch processing of structured data

3. How many people & what talents will you need?
Based on the hypothetical architecture above, these are the talents needed:
    - Software engineer / Data engineer (software architecture / data pipelines / software quality & metrics) (at least 2 seniors)
    - Data scientist (AI and ML - improved bot detection techniques)
    - Data analyst (business analytics)
    - Product Manager (OKR to team objectives mapping - customer requirements/feedback)

4. How will you build a "project" from this (split the work across the people?, follow-up progress, etc.?
    - Identify stakeholders & goals (relevant team OKRs)
    - Translate OKRs into team goals & requirements 
    - Define risks and assess them regularly
    - Identify product solution (epics - user stories)
    - Continuously refine & prioritize requirements (backlog grooming - estimation - sprint planning)
    - Macro (POCs, ADRs) and micro (peer code/design reviews) technical follow-up meetings.
    - Follow-up with the scrum boards on different resolution levels: iteration / sprint / epic
    - Regularly assess improvement impacts on the relevant OKRs
To sum up, making the link between the company OKR framework and the team scope, which is driven by the Scrum framework implemented adequately by the team.
NB. This implies that the company uses the OKR framework, which might not necessarily be the case. The rest remains true otherwise. 

5. How will you attest its quality and health?
Quality assessment should be automated as much as possible. I can imagine for instance automated load tests that run regularly and compare performance results against defined quality thresholds.
Regarding the system health, it is important to maintain health endpoints on the different system components, so that automatic tools can ping those health check endpoints and alert the relevant teams appropriately.
Generally speaking, a robust (and not too verbose) monitoring and alerting system on the infrastructure side is crucial.
Tools like Sentry offer good insights into runtime warnings/errors that the team can easily notice (slack integration) and act upon.
Other tools like Datadog give strong monitoring of infrastructure and applications metrics, for which can be defined flexible alerting rules to define who is alerted and on which basis.

6. Bonus: How would you store the decision results (max. retention would be 10 days)?
It depends on the data itself (size, freshness, etc.) and its usage, the read speed, etc.
Generally, HDFS can also serve the storage of this data (as suggested in the architecture above).
Particular care and precaution should be used when designing the retention policies so that they can be clear, 
easy to implement, predictable, and discoverable 
(the lifecycle of data can be traced effortlessly ie. what policy removed what data and when).
