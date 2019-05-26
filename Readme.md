Data processor engine
=============================
In the financial world we're operating on a term "financial instrument". You can think of it as of a collection of prices of currencies, commodities, derivatives, etc.

For the purpose of this exercise we provide you an input file with multiple time series containing prices of instruments:

-	INSTRUMENT1
-	INSTRUMENT2
-	INSTRUMENT3

File format is as follows:
```
<INSTRUMENT_NAME>,<DATE>,<VALUE>
```
For instance:

`INSTRUMENT1,12-Mar-2015,12.21`
```
TASK:

Read time series from the file provided and pass all of them to the "calculation module".

Calculation engine needs to calculate:

For INSTRUMENT1 – mean
For INSTRUMENT2 – mean for November 2014
For INSTRUMENT3 – sum values
For any other instrument from the input file - sum of the newest 10 elements (in terms of the date).
```
RUNNING
------------

DataProcessor.java run class      


Overview
------------
The projects include reactor lib that could creates Stream of data in order to consume lines and process them "on fly".
All of engine's functions are available to run in parallel and with backpressure params(if needed).

      calculation/Engine   calculation engine wich provides different kind of functions applyed for Flux stream
      model/               packages of Business POJO's
      service/DateParser   facade for parsing data from given file
      resurces/input.txt   file to parse