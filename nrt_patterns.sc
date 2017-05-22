// Very simple example of using patterns + non-real-time synthesis
// Note: this can be executed on the CMD line with sclang
// (no need to s.boot or anything like that!)
var p;
SynthDescLib.read;

SynthDef("NRTsine",{arg freq = 400;
    Out.ar(0,
        SinOsc.ar(freq, 0, Line.kr(0.1, 0.1, 0.25, doneAction: 2))
        )
    }
).writeDefFile;

// pattern
 p = Pbind(\instrument, \NRTsine,
     \freq, Pseq([1, \rest, 1.5, 2, 1.9, 1.8, 1.7, 1.6]*391.995, 1),
     \dur, 0.25);

// render the pattern to wav (4 beats)
p.render("nrt_patterns.wav", 4.0);
