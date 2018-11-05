CryptoBot
===============
Main project for the 2018 year with the goal of learning to use APIs and GUI to create software.

The Program Idea
----------------
Normally, if one comes up with a new trading algorithm, the next step would be to figure out the API used by an exchange of interest and to write a program capable of communicating between the exchange and the trading algorithm. If one wants to use the algorithm on another exchange, this process has to be repeated again.

The idea behind this project is that it is a sort of "plug-and-play" for algorithms. The job of the user is to think of the algorithm. All that is left to do is to select the exchange and market to trade on.

![Program Sample](https://github.com/vasilzhigilei/cryptobot/blob/master/CryptoBot/Sample11October2018.PNG)

This project requires the [XChange](https://github.com/knowm/XChange) Java library for cryptocurrency APIs, as well as the [JFreeChart](https://github.com/jfree/jfreechart) library for data visualization.

Currently, the program is capable of fetching full data of a market on my testing exchange of choice, Cryptopia. Two moving averages are generated, and every 60 seconds the chart updates with new ticker data. Trading via code works in manual tests.

Layout
------

#### Top Toolbar
The window is "undecorated", so there is no default border nor window buttons. In order to move the window, dragging the toolbar allows the user to move the window around the screen. There is currently a working close button, and a maximize and minimize button will be added.

#### Profile/Controls
A "profile" may be created by the user. A profile consists of an exchange, a market, and the user's API Key and API Secret. Profiles may be opened/closed in the chart, activated/deactivated, or added/deleted by the user.
Controls is an unfinished tab which is intended to have all the controls the user might want for the selected profile. This may include importing an algorithm, selecting and setting indicators, training, and more.

#### The Chart
Displays ticker close price for each timestamp. Currently only has simple moving averages, but more indicators will be added.

#### Console
The primary and secondary consoles will be used as output when the program is running. The primary console is reserved for "trading" information, while the secondary console is reserved for more "behind the scenes", or debugging, information.

#### Processes
This currently blank pane is intended to display processes going on in the program. Things like generating long period moving averages on very dense data may take a while, and therefore it is important to display processes. The code for the processes is nearly complete.

More Information
----------------

This program is not going to be limited to only cryptocurrency exchanges. If an exchange has both public and private API, it will be possible to add the exchange to the program database.

History
-------
##### Version 0.1.1 (15 October, 2018)
* Added doc comments to methods

##### Version 0.1 (11 October, 2018)
* Fixed JSON parsing of Cryptopia ticker data due to server changes
