# Flexible, pipeline-based input handling.

`iota` is a small, lightweight abstraction over the default WPILib functions
for handling joystick inputs, and extends its functionality to any kind
of input, whether through SmartDashboard, joystick, or sensor.  Any value
that can be polled can be handled by `iota`.

Furthermore, `iota` provides an elegant, simplistic pipeline implementation
in which reusable steps can be chained together.  Often, inputs are not
directly applied to motor speeds; they are modified somehow.  With `iota`,
this occurs by passing the input through a pipeline and then running a
callback with the resulting, transformed input.

### Notes on the name.
`iota` started out simply named `io`.  However, this conflicts with `io`,
which is a Python core library, so trying to import `io` would implement the
core library instead of what is actually wanted.  The word "iota" contains
"io", means "a very small amount" (the main code, `iota.py`, is only 72 SLOC
uncommented), and is pretty easy to type (being 4 letters long),
so I just decided to name it that.

### Notes on the language
`iota`, like all the other code written by FRC Team 5002 from the 2017-2018
season onwards, is written in Python.  In fact, `iota` was a major reason
why we switched from Java to Python; Python allowed us much more flexibility
with its dynamic typing and tuples than Java ever could.  In fact, just
the pipeline implementation in Java was already longer than the entire `iota`
in Python, and heavily generics-based and unreadable. With Python, "it just
works".

### Notes on the usage
Most detailed information on the usage of `iota` can be found in this
documentation.  This is only meant as a quick overview.

`iota` is used by constructing a pipeline and adding it to the poller.
For example,

```python
from iota import *

pipe = Pipeline()
pipe.add(lambda x : not x, float)
Poller.on(gamepad1.x, Events.PRESSED, pipe, setServoSpeed)
```

where `gamepad1.x` is a function that returns the value of `x`,
Events.PRESSED is an enum value telling the poller to only run the pipeline
when a PRESSED action has been detected, `pipe` is the pipeline of steps,
and `setServoSpeed` is an example callback that actually applies the changes
to the hardware.

More information can be found in documentation.
