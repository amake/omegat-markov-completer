# omegat-markov-completer
An autocompleter view backed by a Markov Generator (yes, it's a joke)

![screenshot](https://amake.github.io/omegat-markov-completer/screenshot.png)

This autocompleter view generates suggestions via an order-5 (in characters)
Markov generator trained on all existing translations* in the project. It is
also trained on new translations as they are added**.

\*"Default" translations only  
\*\*Changes to translations are ignored

## Requirements
OmegaT 3.5 or newer (only tested on trunk)

## Obtaining
Download the JAR from [Releases]
(https://github.com/amake/omegat-markov-completer/releases) or build yourself.

## Building
Clone the repository, then run `mvn install`.

## Installing
1. Place the JAR in one of OmegaT's `plugins` directories (alongside
`OmegaT.jar`, in the configuration directory, etc.).
2. Enable the view in `Options` > `Machine Translate` > `Markov Translator`.

## License
This project is distributed under the [GNU General Public License, v3]
(http://www.gnu.org/licenses/gpl-3.0.html).


Copyright 2015 Aaron Madlon-Kay <aaron@madlon-kay.com>
