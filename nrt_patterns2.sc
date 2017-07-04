// Very simple example of using patterns + non-real-time synthesis
// Note: this can be executed on the CMD line with sclang
// (no need to s.boot or anything like that!)
var p1, p2, pp, odir, p3, p4, dur1, dur2, s1, s2, ext, reps, scr, pp1, pp2
, p5, p6, p7, p8, pp3, pp4, s3, p9, s4, p10, pp5;

// Set output path
odir = thisProcess.nowExecutingPath.dirname;

SynthDef(\syn1, { |freq = 440, amp = 1, scal = 0.5|
    var sig, env;
	sig = SinOsc.ar([freq*6/8, freq*10/8], 0, 0.3);
	env = Env([0,0.9,0.9,0], [0.07, 0.01, 0.25]*scal);
	env = EnvGen.kr(env, doneAction: 2);
	sig = Out.ar(0, sig*env);
}).writeDefFile;

// pattern
ext = 2; // Length of smallest repeating unit
reps = 2; // # times to repeat this unit
s1 = [8/8, \rest, 7/8, \rest, 6/8, \rest, 5/8, \rest];
s2 = [2/8, 4/8, \rest, 8/8, 10/8, \rest, 14/8, 16/8];
s3 = [7/7, 7/6, 7/5, \rest, 7/4, 7/3, 7/2, 7/1];
s4 = [7/7, 6/7, 5/7, \rest, 4/7, 3/7, 2/7, 1/7];

// 1st
p1 = Pbind(\instrument, \syn1,
     \freq, Pseq(s1*440, ext),
	\dur, (1/s1.size)*1);
p2 = Pbind(\instrument, \syn1,
	\freq, Pseq(s1*440, 1),
	\dur, (1/s1.size)*ext);

// 2nd
p3 = Pbind(\instrument, \syn1,
     \freq, Pseq(s2*440, ext),
	\dur, (1/s1.size)*1);
p4 = Pbind(\instrument, \syn1,
	\freq, Pseq(s2*440, 1),
	\dur, (1/s1.size)*ext);

// 3rd
p5 = Pbind(\instrument, \syn1,
	\freq, Pseq(s1*440*(10/8), ext),
	\dur, (1/s1.size)*1);
p6 = Pbind(\instrument, \syn1,
	\freq, Pseq(s1*440*(6/8), 1),
	\dur, (1/s1.size)*ext);

// 4th
p7 = Pbind(\instrument, \syn1,
     \freq, Pseq(s2*440*(10/8), ext),
	\dur, (1/s1.size)*1);
p8 = Pbind(\instrument, \syn1,
	\freq, Pseq(s2*440*(10/8), 1),
	\dur, (1/s1.size)*ext);

// 5th
p9 = Pbind(\instrument, \syn1,
	\freq, Pseq(s3*440, ext),
	\dur, (1/s1.size)*1);

// 6th
p10 = Pbind(\instrument, \syn1,
	\freq, Pseq(s4*440, 1),
	\dur, (1/s1.size)*ext);

// Combining things in parallel
pp1 = Ppar([p1, p2]);
pp2 = Ppar([p3, p4]);
pp3 = Ppar([p5, p6]);
pp4 = Ppar([p7, p8]);
pp5 = Ppar([p9, p10]);

// Combining things in series
scr =
[p8, pp1, p6, pp2, p4, pp3, p2, pp4,
p7, pp1, p5, p9, p3, p9, p1, pp4,
p10, p6, p10, p8, p9, p9, p4, p10];

pp = Pseq(scr, reps);

// render the pattern to wav
pp.render(
    path: odir +/+ "nrt_patterns2.wav"
	, maxTime: ext*reps*scr.size
	, headerFormat: "WAV"
);
