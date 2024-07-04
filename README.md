# Vega trading system test task

## Introduction

Being an applicant I am asked to write a simple trading system with several constraints.<br>
Taking into account the fact I know nothing about trading systems I'm glad I have this challenge anyway.<br>
It was the best several days I spend working on this task I have for the last six months.<br>
I decided not to bring dependency injection framework in my solution and just use Java lang and stick functional programming style as mush as possible.

## Getting Started

### Installation and run

- Need java 17 to compile, gradle-8.5 to build
- No runnable class is given, use test classes to run code.

## How to test

No classic tests are written (e.g. given/when/then test cases).
In test folder I just wrote test scenarios.
They could be run inside Intellij Idea IDE directly.

## Application design in several words.
When a composite order having three Fin Insrt is placed it is considered to be completed only for the case if underlying stocks
orders are matched. 
I did a little assumption that the match is not enough, and it is good to have an additional checks for account's balance and Fin Instr quantity.
If two of three Fin Inst in one Composite Fin Inst have successfully been traded but the third one was failed then there should be a rollback operations executed.
I decided to execute a trade on a temporary values and if all three Fin Instr were successfully processed, then the temporary results (portfolio's content and balance) could be transferred on to the real values.
If one of three Fin Instr was not successfully processed, then the temporary results could be just ignored, thus preventing the whole portfolio's state unchanged.
In pseudo-code it could be presented as follows:
- orderValidation();
- executeOrders();
- updateAccountWithNewValues(); 

## Concerns I would review if I was writing a real application.

Since I have a limited amount of time I have implemented several things in a simple way.
I will name them and provide a quick explanation how it should go in real application.

- processOrder() and cancelOrder() should be able to work with a collection of Orders in thread safe way in a real application.
The whole OrderBook object implementation is not supported to work in multithreaded mode in this test application.
- to be able to do equals operations on Financial Instrument objects I implemented a very simple logic for equals() and hashCode() methods which brings me O(log N) search complexity.
Another way to find a certain FinInstr is to have them all in a List collection and just have iteration operation throw the all elements, but the search complexity is O(n) in that case.
