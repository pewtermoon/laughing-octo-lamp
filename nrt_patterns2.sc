// Very simple example of using patterns + non-real-time synthesis
// Note: this can be executed on the CMD line with sclang
// (no need to s.boot or anything like that!)
var p1, p2, pp;

SynthDef(\syn1, { |freq = 440, amp = 1, scal = 0.5|
    var sig, env;
	sig = SinOsc.ar([freq-15, freq+15], 0, 0.3);
	env = Env([0,0.9,0.9,0], [0.07, 0.01, 0.25]*scal);
	env = EnvGen.kr(env, doneAction: 2);
	sig = Out.ar(0, sig*env);
}).writeDefFile;

// pattern
p1 = Pbind(\instrument, \syn1,
     \freq, Pseq([1, \rest, 1.5, 2, 1.9, 1.8, 1.7, 1.6]*391.995, 8),
     \dur, 0.25);

p2 = Pbind(\instrument, \syn1,
     \freq, Pseq([2, 1.9, 1.8, \rest, 0.9, 1.1, 1.5, 1]*261.626, 4),
     \dur, 0.5,
     \scal, 1);

pp = Ppar([p1, p2], 16);

// render the pattern to wav
pp.render(path: "nrt_patterns2.wav", maxTime: 16.0, headerFormat: "WAV");
