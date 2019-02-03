## Shell interpreter architecture-shmarchitecture:

Data-flow diagram.
![Data-flow diagram](https://raw.githubusercontent.com/karvozavr/software-design-spring-2019/hw1-cli/ShellDataDlow.png)

System decomposition.
![System decomposition](https://raw.githubusercontent.com/karvozavr/software-design-spring-2019/hw1-cli/SystemArchitecture.png)

Executable unit is a Pipeline. 
Pipeline executes commands sequentially, redirecting output of one command to input of next command.
First command receives input from stdin and the last command writes output to stdout.
Every command in pipeline is being executed in it's own shell context.