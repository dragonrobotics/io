import types
import time
from enum import Enum

class Poller:

    _listenables = []
    _results = []
    _times = []


    def on(input_callback, event_name, pipeline, callback):
        '''
        Test doxypypy.

        This is an example of how a Pythonic human-readable docstring can
        get parsed by doxypypy and marked up with Doxygen commands as a
        regular input filter to Doxygen.

        Args:
            input_callback:   A positional argument.
            arg2:   Another positional argument.

        Kwargs:
            kwarg:  A keyword argument.

        Returns:
            A string holding the result.

        Raises:
            ZeroDivisionError, AssertionError, & ValueError.

        Examples:
            >>> myfunction(2, 3)
            '5 - 0, whatever.'
            >>> myfunction(5, 0, 'oops.')
            Traceback (most recent call last):
                ...
            ZeroDivisionError: integer division or modulo by zero
            >>> myfunction(4, 1, 'got it.')
            '5 - 4, got it.'
            >>> myfunction(23.5, 23, 'oh well.')
            Traceback (most recent call last):
                ...
            AssertionError
            >>> myfunction(5, 50, 'too big.')
            Traceback (most recent call last):
                ...
            ValueError
        '''
        _listenables.append( (input_callback,
                              event_name,
                              Pipeline(pipeline),
                              callback) )
        _results.append(None)
        _times.append(0)


    def tick():

        def get_current_millis():
            return int(time.time() * 1000)

        for index, listenable in enumerate(_listenables):
            input_callback, event_name, pipeline, callback = listenable
            new = input_callback()

            if event_name == Events.ALWAYS:
                callback(pipeline.run(new))
                continue
            elif event_name == Events.CHANGED:
                if _results[index] != new:
                    _results[index] = new
                    callback(pipeline.run(new))
                continue

            if float(new) != 0.0:
                if float(_results[index]) == 0.0:
                    _times[index] = get_current_millis()
                    _results[index] = 1.0
                elif float(_results[index]) == 1.0:
                    if (get_current_millis() - _times[index]) >= 150:
                        if event_name == Events.PRESSED:
                            callback(pipeline.run(new))
                            _results[index] = 2.0
            else:
                if float(_results[index]) == 2.0:
                    if event_name == Events.RELEASED:
                        callback(pipeline.run(new))
                        _results[index] = 0.0
                elif float(_results[index]) == 1.0:
                    if event_name == Events.TAPPED:
                        callback(pipeline.run(new))
                        _results[index] = 0.0


class Events(Enum):
    ALWAYS = 0
    CHANGED = 1
    PRESSED = 2
    RELEASED = 3
    TAPPED = 4


class Pipeline:

    def __init__(self, *steps):
        self.steps = []
        self.add(steps)

    def add(self, *steps):
        for step in steps:
            if isinstance(step, (tuple, list)):
                self.add(*step)
            elif isinstance(step, Pipeline):
                self.add(step.steps)
            elif isinstance(step, (types.FunctionType,
                                   types.LambdaType,
                                   types.BuiltinFunctionType)):
                self.steps.append(step)
        return self

    def run(self, value):
        x = value
        for step in self.steps:
            x = step(x)
        return x
