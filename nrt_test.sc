// Very simple example of using non-real-time synthesis
// Note: vars must be declared at the top of the script, else error
var f, o;
g = [
    [0.1, [\s_new, \NRTsine, 1000, 0, 0, \freq, 440]],
    [1, [\c_set, 0, 0]]
    ];
o = ServerOptions.new.numOutputBusChannels = 2; // stereo output
Score.recordNRT(g, "nrt_test.osc", "nrt_test.wav", options: o);

// the 'NRTsine' SynthDef
SynthDef(
    "NRTsine",
    {
        arg freq = 440;
        Out.ar(0, SinOsc.ar(freq, 0, 0.2))
    }
).writeDefFile;

