// Very simple example of using non-real-time synthesis
// Note: vars must be declared at the top of the script, else error
var f, o;

// Synth def, which is written to file. Don't forget Out.ar!
SynthDef("NRTwn",{arg freq = 1000;
    Out.ar(0,
        LPF.ar(WhiteNoise.ar(0.1), freq)
        )
    }
).writeDefFile;

SynthDef("NRTsine",{arg freq = 1000;
    Out.ar(0,
        SinOsc.ar(freq, 0, Line.kr(0.1, 0.1, 0.25, doneAction: 2)) // stops after xs
        )
    }
).writeDefFile;

// Write score (this syntax is a score)
// Score args: timing [name of OSC cmd, synth, node id, addAction, targetID, synth args ... ]
g = [
    [0.0, [\s_new, \NRTsine, 1000, 0, 0, \freq, 400]],
    [0.5, [\s_new, \NRTsine, 1001, 0, 0, \freq, 500]],
    [1.0, [\s_new, \NRTsine, 1002, 0, 0, \freq, 600]],
    [1.5, [\s_new, \NRTsine, 1003, 0, 0, \freq, 500]],
    [2.0, [\s_new, \NRTsine, 1004, 0, 0, \freq, 400]],
    [2.5, [\s_new, \NRTsine, 1005, 0, 0, \freq, 300]],
    [3.0, [\s_new, \NRTsine, 1006, 0, 0, \freq, 200]],
    [3.5, [\s_new, \NRTsine, 1007, 0, 0, \freq, 100]],
    [4.0, [\c_set, 0, 0]] //dummy, ends thing
    ];
    
// Specify server options (number of channels)
o = ServerOptions.new.numOutputBusChannels = 1; // mono output

// Write score to file
Score.recordNRT(g, "nrt_test2.osc", "nrt_test2.wav", options: o);

