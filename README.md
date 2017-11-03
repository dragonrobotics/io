<h1 align="center">io :unicorn:</h1>

**io** is an experimental system for handling inputs of all types, whether by joystick or sensor. It is based off of the
[Pipeline Design Pattern](https://www.cise.ufl.edu/research/ParallelPatterns/PatternLanguage/AlgorithmStructure/Pipeline.htm),
in which inputs can be passed through a series of stages to be transformed in an elegant fashion.

Furthermore, **io** abstracts away raw input sources, allowing:
- sensor inputs to be treated exactly the same as gamepad inputs, and
- gamepad inputs to be easily remapped without having to recompile the code.

### Progress Update
Unfortunately, due to random bugs and an underestimation on the time frame, **io** is _still_ not done even after moving the 
deadline back once.  But here's a progress update anyways.

#### So far, I have implemented:
- a flexible, typesafe, generic pipeline implementation allowing for full flexibility in chaining together different pipeline 
stages of different types.
- `Input` and `InputGroup` base classes.

#### Currently, I am working on (and almost done with):
- a configuration file parser. This is a custom built parser for reading and interpreting Pipeline Markup Language files at 
runtime.  PML files are nifty little things invented by me, and they are a very concise yet readable way of representing inputs,
pipelines, and callbacks in easily readable text, instead of manually building each pipeline for each input in code.

Here is some example PML syntax: 
```text
# Example Comment
gamepad1.left_joystick_x :: changed |-> foc -> deadband(0.5) ->| strafe;
gamepad1.left_joystick_y :: always  |-> foc ->| forward;

# Below demonstrates the ability to put multiple listeners on the same input.
navx.heading
    :: changed |-> smoothing -> scaling(5, 6, 7, 8) ->| value
    :: always |->| logging  # empty pipeline
    ;
```
The basic syntax for a pipeline declaration is:
```text
<device name>.<input name>::[<optional filter, "changed" by default>] |-> <stages> ->| <callback name>;
```
Currently the main focus is keeping the parser as small and simple as possible.  Efficiency isn't really a top priority; the .pml
file is only read once during the initialization of the robot, and it is my understanding that the speed at which it does this 
isn't really a big issue.  The `ConfigReader` will also handle interpreting the parsed data and automatically constructing the 
required pipelines based on the PML file.

The PML file does not eliminate the ability to create pipelines directly in Java code; I will still try to make the Java code for
doing this as elegant as possible using functional interfaces, etc., _but it will not even be nearly as readable and concise
as the PML syntax_.

#### What still needs to be done:
- Implementing Factory-style manager classes for the parser to be able to connect string representations to actual classes.
- Implementing a tick-based input poller.  I have a very primitive one written right now, but it most likely doesn't work and it 
most definitely does not allow for listening for more complex events.
