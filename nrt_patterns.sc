// Very simple example of using patterns + non-real-time synthesis
// Note: this can be executed on the CMD line with sclang
// (no need to s.boot or anything like that!)
var p;
SynthDescLib.read;

// SynthDef("NRTsine",{arg freq = 400;
//     Out.ar(0,
//         SinOsc.ar(freq, 0, Line.kr(0.1, 0.1, 0.25, doneAction: 2))
//         )
//     }
// ).writeDefFile;

SynthDef(\syn1, { |freq = 440, amp = 1, scal = 0.5|
    var sig, env;
	sig = SinOsc.ar([freq-15, freq+15], 0, 0.3);
	env = Env([0,0.9,0.9,0], [0.07, 0.01, 0.25]*scal);
	env = EnvGen.kr(env, doneAction: 2);
	sig = Out.ar(0, sig*env);
}).writeDefFile;

// pattern
 p = Pbind(\instrument, \syn1,
     \freq, Pseq([1, \rest, 1.5, 2, 1.9, 1.8, 1.7, 1.6]*391.995, 4),
     \dur, 0.25);

// render the pattern to wav
p.render(path: "nrt_patterns.wav", maxTime: 8.0, headerFormat: "WAV");
