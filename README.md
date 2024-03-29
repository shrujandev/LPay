>#### UE20CS352 - Object Oriented Analysis and Design with Java: Mini Project
> # Lpay - a UPI Clone: Simulation of an instant real-time payment system
> UPI has recently taken over as the primary method of transactions for businesses and people in both small and large scales. The brainchild of Dr Raghuram Rajan, this system was first introduced with just 21 banks had 0 transactions for 3 months, but now is leading the chart among payment handling systems globally.
> 
>The UPI System handles payments using a middleman server known as the NPCI server. Registered link their bank accounts to their UPI accounts and add a UPI pin. This pin is used for authentication when the user intiates a transaction. The NPCI server handles the transaction request by contacting the bank servers of the accounts involved in the transaction and delegating the transfer of funds to the banks.
>
>This project is the result of our exploration and understanding of the UPI System. Our project simulates all the essential functions of the UPI System such as registrations, transactions and logging. We are simulating the environment of the system by creating bank server APIs and client systems. Communication between each of these components is logged. Logs are maintained for every transaction handled. A front end interface cleanly displays relevant user information such as current bank balance. UPI ids are generated for every new user upon registration. 
>
>### System architecture:
>The system is built as 3 separate servers and 1 client. All systems are built in java using the spring boot framework. Front end is built using the thymeleaf plugin for the spring boot framework. The servers are designed as REST APIs. MySQL is used for database services.
>### Use Case Diagram:
>![Use Case Diagram](diagrams/useCase.jpg?raw=true "Use Case Diagram")
>### Class Diagrams:-
>- #### UPI Client
>![Class Diagram](diagrams/classDiagramClient.jpg?raw=true "Client class Diagram")
>- #### UPI Server
>![Class Diagram](diagrams/classDiagramUPIServer.jpg?raw=true "Client class Diagram")
>-  #### NPCI Server
>![Class Diagram](diagrams/classDiagramNPCIServer.jpg?raw=true "Client class Diagram")
>-  #### Bank Server
>![Class Diagram](diagrams/classDiagramBankServer.jpg?raw=true "Client class Diagram")
>### Activity diagram
>![Activity Diagram](diagrams/ActivityDiagram.jpg?raw=true "Activity Diagram")
>### State Diagrams:-
>- #### Register
>![State Diagram](diagrams/StateDiagramRegister.jpg?raw=true "Register State Diagram")
>- #### Login
>![State Diagram](diagrams/StateDiagramLogin.jpg?raw=true "Login state Diagram")
>- #### Transaction
>![State Diagram](diagrams/StateDiagramTransaction.jpg?raw=true "Transaction state Diagram")
