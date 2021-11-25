# StockHolmes
A software system to automatically digest real-world data, infer on it and predict stock behavior accordingly.

# Technology
The entire project is written in Java 16 and uses MySQL server. It might have some research code written in Python, and the input files are mostly written in JSon format.

# Topology
The project runs a main function that retrieves all the necessary parameters from a JSon file. It contains a configuration part, a setup part for all the processes to be run on startup and a jobs part, that specifies which job runs when and with which parameters. The jobs can run either at startup, right after another job has ended or as a scheduled task.

# Infrastructure
Currently, the project is run on a local machine and stores data on the local hard drive. As we progress, it is planned that the project would run continuously on AWS and use a permanent storage means, such as Amazon S3. The project is designed to be agnostic regarding the machine and storage, and they should be easily exchanged using configuraiton parameters files.

# Components
## Data Retrieval
This job collects data from the outside world, such as stock data (from https://www.alphavantage.co/), company press releases, web pages, Twitter, Facebook, etc. All the data is stored in the database as raw data, with a specific table(s) for each type of data.
## Feature Extractor
Converts the unsorted raw data into well defined features for each stock and day. While the stock and date are well defined, the features might be endless, and each kind of extractor may create any number of new features.
## Model Trainer
Once all the data is encapsulated into features, we can choose any model we want to train on these data, train, save the model file itself on S3 (or local storage, at the moment) and document all the metadata both on the model and on the training process in the database. These models actually predict the price of a stock a certain amount of days in advance based on historical data.
## Model Grader
Each model might be tested for performance against unseen data and a grade would be determined and saved in the database.
## Startegy Chooser
However important, a model predicting stock price is not enough. Since these models would be far from accurate, we need to come up with a smart way to utilize them and eventually make concrete trading decisions. Therefore the final objective is a trading strategy, that would use a mixture of models, apply them on the data it already has and according to some parameters produce concrete trading orders. For this purpose we shall apply a genetic algorithm that would create and emphasize strategies with a set of parameters such that their trading behavior is closer to optimal than random, with an expectation and hope to improve them provided more data and models. In general, a tournament may be held with the strategies competing for optimally trading on historic data, over and over again throughout the algorithm generations, eventually improving the strategy population and coming up with a good strategy.
## Strategic Advisor
The final input of the machinary, the Strategic Advisor applies a certain number of selected strategies, which scored highest on the Strategy Chooser tournaments, on actual updated stock data, providing a service of a real life stock trading advisor. At first, we can get an impression on their performance, and if they perform well enough in the long run, a single selected strategy will be the final product output of the system, and real money will be invested using this advisor.
