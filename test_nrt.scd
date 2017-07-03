// Very simple example of using non-real-time synthesis
// Note: vars must be declared at the top of the script, else error
var f, o;

// the 'NRTsine' SynthDef
SynthDef(
    "NRTsine",
    {
        arg freq = 440;
        Out.ar(0, SinOsc.ar(freq, 0, 0.2))
    }
).writeDefFile;

// the score
// Score args: timing [name of OSC cmd, synth, node id, addAction, targetID, synth args ... ]
g =
[
    [0.0, [\s_new, \NRTsine, 1000, 0, 0, \freq, 440]]
    , [2.0, [\s_new, \NRTsine, 1001, 0, 0, \freq, 880]]
    , [4, [\c_set, 0, 0]]
];

// Server options
o = ServerOptions.new.numOutputBusChannels = 2; // stereo output



// Write to file
Score.recordNRT(g
	, "nrt_test.osc"
	, thisProcess.nowExecutingPath.dirname +/+ 'nrt_test.wav';
	, options: o
);