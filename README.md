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
- A PML configuration parser.
- an extension over HashMap doing null checks and key verification.

#### What still needs to be done:
- Implementing Factory-style manager classes for the parser to be able to connect string representations to actual classes.
- Implementing a tick-based input poller.  I have a very primitive one written right now, but it most likely doesn't work and it
most definitely does not allow for listening for more complex events.
