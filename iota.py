# Copyright (c) 2017 Brandon Gong
#
# Permission is hereby granted, free of charge, to any person obtaining a copy
# of this software and associated documentation files (the "Software"), to deal
# in the Software without restriction, including without limitation the rights
# to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in all
# copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
# AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
# OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
# SOFTWARE.

## @package iota
# Flexible, pipeline-based input handling.
#
# @author Brandon Gong


import types
import time
from enum import Enum


## Poll inputs and listen for changes.
#
# This class will poll inputs and listen for changes, and then run pipelines
# and callbacks once a change is detected.
# This class is also responsible for detecting and recognizing different,
# more high-level types of inputs, which are defined in the `Events` class.
class Poller:

    # lists for storing various things.
    _listenables = []
    _results = []
    _times = []

    ## Add an input to the poller.
    #
    # When the poller recognizes the event, it will pass the input through
    # the pipeline and call the callback with the resulting value.
    #
    # @param input_callback The callback for getting the input. This can be a
    #                       function that returns
    #                       `wpilib.Joystick.getRawAxis(x)`, for example.
    # @param event_name     An `Events` enum value describing what to
    #                       listen for.
    # @param pipeline       The pipeline. This is passed directly into a
    #                       `Pipeline` constructor. Thus, this value can be of
    #                       any type supported by the `Pipeline.add()` function.
    # @param taplength      Define how long, in milliseconds, a tap is.  This is
    #                       by default 150ms.  You can also set taplength to
    #                       0ms to make the poller call Events.PRESSED
    #                       immediately, if you really don't care for the
    #                       distinction between tapping and pressing.
    def on(input_callback, event_name, pipeline, callback, taplength=150):

        # Add all those arguments to the _listenables list as a tuple.
        #
        # Notice that we reinstantiate the Pipeline; this is intentional.
        # Refer to the Pipeline documentation for more information on how
        # it increases flexibility.
        _listenables.append( (input_callback,
                              event_name,
                              Pipeline(pipeline),
                              callback,
                              taplength) )

        # and then grow the helper lists to the same size.
        _results.append(None)
        _times.append(0)


    ## Poll all the inputs.
    #
    # This function probably shouldn't be called anywhere besides in a
    # specialized Command class that is run over and over again by the main
    # Robot class.  However, it should be called often, as much of the
    # functionality depends on millisecond speeds to measure and detect
    # high-level input patterns.
    # **TODO**: Perhaps consider polling and executing pipelines
    # asynchronously. This isn't implemented yet because it might not be
    # necessary (I doubt it will be), and it might not even be a performance
    # boost depending on the roborio hardware.
    def tick():

        # a little helper function so we don't have to write out the whole
        # time computation every time.
        def get_current_millis():
            return int(time.time() * 1000)

        # FIXME: this is the ugliest part of the code. there is far too much
        # nesting to be considered good python style.
        # Furthermore, the logic is quite difficult to understand and also prone
        # to hidden bugs.

        # iterate through listeners. We use `enumerate` here because we use the
        # index to access corresponding values in the _results and _times lists.
        for index, listenable in enumerate(_listenables):

            # unpack the listenable tuple for the sake of readability.
            input_callback,event_name,pipeline,callback,taplength = listenable

            # get the new value.
            new = input_callback()

            # if the event is ALWAYS, run the pipeline with the new value.
            if event_name == Events.ALWAYS:
                callback(pipeline.run(new))
                continue

            # the idea for CHANGED is very similar except we compare with the
            # _results value, which stores the value of the previous tick,
            # and only run the pipeline if it has changed.
            elif event_name == Events.CHANGED:
                if _results[index] != new:
                    _results[index] = new
                    callback(pipeline.run(new))
                continue

            # the below code handles all of the other Event listeners.
            # Here, _results takes on a new meaning:
            #    0.0: input has been released; starting state.
            #    1.0: input has been pressed but the PRESSED callback has not
            #         been fired yet.
            #    2.0: input has been pressed and PRESSED callback has been
            #         fired.
            # float() will convert boolean inputs to 0.0 or 1.0, keep range
            # inputs the same, and then throw an error if neither of those
            # work.

            # if the input is currently pressed on this tick
            if float(new) != 0.0:

                # was the input released on the last tick?
                if float(_results[index]) == 0.0:

                    # if taplength is 0 and we are listening for the PRESSED
                    # event, call it right away.
                    # Making a special case for taplength of 0 is a minor
                    # performance improvement and saves one tick.
                    if taplength == 0 and event_name == Events.PRESSED:
                        callback(pipeline.run(new))
                        # set _results to 2.0; we've already fired the event.
                        _results[index] = 2.0

                    else:
                        # record the current millis in the _times list.
                        # this is the exact moment the input has been pressed
                        # down.
                        _times[index] = get_current_millis()

                        # and then change the _results to 1.0 to signal that we
                        # have recorded the press event
                        _results[index] = 1.0

                # If we've already recorded a press action, but we haven't fired
                # the event yet:
                elif float(_results[index]) == 1.0:

                    # we just need to check if taplength has passed.
                    if (get_current_millis() - _times[index]) >= taplength:

                        # if that period has passed, and we are listening for a
                        # PRESSED event, then fire the event and set _results
                        # to 2.0.
                        if event_name == Events.PRESSED:
                            callback(pipeline.run(new))
                            _results[index] = 2.0

                # if _results is 2.0, then we've already recorded the action
                # and fired the event, so theres nothing to do here.

            # else if the input is 0.0:
            else:

                # if the PRESSED event has already been fired, we now need to
                # fire the corresponding RELEASED event.
                if float(_results[index]) == 2.0:
                    if event_name == Events.RELEASED:
                        callback(pipeline.run(new))

                        # and then reset _results back to 0.0
                        _results[index] = 0.0

                # if the PRESSED event has not yet been fired, but a press
                # action has been recorded, we just need to fire a TAPPED event.
                elif float(_results[index]) == 1.0:
                    if event_name == Events.TAPPED:
                        callback(pipeline.run(new))

                        # and then reset _results back to 0.0
                        _results[index] = 0.0

                # if _results is 0.0, no press action has been recorded, so
                # we just assume that that certain input hasn't been pressed.
                # There's no release events that we need to handle.


## Define high-level events for polling.
#
# This enum defines 5, higher-level event patterns that the poller can
# recognize. This includes `ALWAYS`, `CHANGED`, `PRESSED`, `RELEASED`, and
# `TAPPED`.  Passing anything other than these values in the second parameter
# of the `Poller.on()` function will result in an error.
class Events(Enum):

    ## ALWAYS run the pipeline, no matter what.
    # This is not very efficient.
    ALWAYS = 0

    ## Only run the pipeline when the input has CHANGED.
    # When in doubt, use this.
    CHANGED = 1

    ## Run the pipeline when a PRESSED action has been detected.
    # This descriptor only applies to boolean and range values.  You can use
    # custom classes, you just have to be able to make it work with the built-in
    # float() function.
    PRESSED = 2

    ## The counterpart event to the PRESSED action.
    # With every press comes a release.
    RELEASED = 3

    ## When the button/range input has been tapped, but not held sufficiently
    # long enough to be considered a PRESSED event.
    TAPPED = 4


## A simplistic but flexible pipeline implementation.
class Pipeline:

    ## Create a new pipeline.
    # This initializes the steps[] list and adds in any steps that may have been
    # passed through the constructor by calling the `add` function.
    #
    # @param steps The steps to be passed into the pipeline.
    def __init__(self, *steps):
        self.steps = []
        self.add(steps)

    ## Add steps to the pipline.
    #
    # This is a very flexible recursive function that adds steps to the
    # pipeline. You can pass functions, built-in functions, lambda expressions,
    # other pipelines, or tuples/lists containing any of those to the `add`
    # function and they will all be appended to the pipeline beautifully.
    #
    # This allows for chaining together frequently used pipeline segments and
    # other steps in a very easy syntax, among other things.
    #
    # @param steps The steps to be added to the pipeline.
    # @return the pipeline after adding the steps, for optional chaining.
    def add(self, *steps):

        # iterate through steps.
        for step in steps:

            # unpythonic but necessary instance checks.  If it's a tuple or a
            # list, break it up and pass it in to the `add` function again as
            # varargs.
            if isinstance(step, (tuple, list)):
                self.add(*step)

            # else if the step is a pipeline, call the add method with that
            # pipeline's steps.
            elif isinstance(step, Pipeline):
                self.add(step.steps)

            # else if it's any of those function types, just add it. The
            # callable function is the lowest building block of a pipeline,
            # and we can't recurse any lower.
            elif isinstance(step, (types.FunctionType,
                                   types.LambdaType,
                                   types.BuiltinFunctionType)):
                self.steps.append(step)

        # return this instance for optional chaining; however this shouldn't
        # really be necessary given the varargs flexibility of the `add`
        # function.
        return self

    ## Run this pipeline.
    # This is where Python's dynamic typing gives it the edge over Java.
    #
    # @param value the starting value to be passed into the pipeline.
    # @return the value after it has been passed through all of the steps in
    #         the pipeline.
    def run(self, value):
        x = value
        for step in self.steps:
            x = step(x)
        return x
