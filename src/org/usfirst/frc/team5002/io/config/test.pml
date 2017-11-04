gamepad1
 .left_joystick_x
     	:: always |
     	
->foc-> 	
							deadband
     	(
     		0.5
     	) ->
     	
    |strafe,
	     |->
	     
	 | log, .left_joystick_y
		|-> foc-> | forward
	;

# Comment                   #line
navx
 
														.heading
    :: sometimes |-> smoothing
    								-> scaling(5
    
    
    
    
    
    
    
    , 6,7
    
    , 8)
    
 -> asdf ->           | value,
  :: always |
    ->
    |  log 		# comments are allowed on the same line as well
;
										