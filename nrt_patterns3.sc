// Very simple example of using patterns + non-real-time synthesis
// Note: this can be executed on the CMD line with sclang
// (no need to s.boot or anything like that!)
var p1, p2, pp, odir, p3, p4, dur1, dur2, s1, s2, ext, reps, scr, pp1, pp2
, p5, p6, p7, p8, pp3, pp4, s3, p9, s4, p10, pp5, s5, s6, s7, s8,
p11, p12, p13, p14, p15, p16, pp6, pp7, pp8, ffrq;

// Set output path
odir = thisProcess.nowExecutingPath.dirname;

ffrq = 440;

SynthDef(\syn1, { |freq = 440, amp = 1, scal = 0.5|
    var sig, env;
	sig = SinOsc.ar([freq/0.98, freq/1.02], 0, 0.3);
	env = Env([0,0.9,0.9,0], [0.07, 0.01, 0.25]*scal);
	env = EnvGen.kr(env, doneAction: 2);
	sig = Out.ar(0, sig*env);
}).writeDefFile;

// pattern
ext = 2; // Length of smallest repeating unit
reps = 2; // # times to repeat this unit
s1 = [1/8, \rest, 2/8, \rest, 3/8, \rest, 4/8, \rest];
s2 = [1/7, \rest, 2/7, \rest, 3/7, \rest, 4/7, \rest];
s3 = [1/6, \rest, 2/6, \rest, 3/6, \rest, 4/6, \rest];
s4 = [1/5, \rest, 2/5, \rest, 3/5, \rest, 4/5, \rest];
s5 = [1/4, \rest, 2/4, \rest, 3/4, \rest, 4/4, \rest];
s6 = [1/3, \rest, 2/3, \rest, 3/3, \rest, 4/3, \rest];
s7 = [1/2, \rest, 2/2, \rest, 3/2, \rest, 4/2, \rest];
s8 = [1/1, \rest, 2/1, \rest, 3/1, \rest, 4/1, \rest];

// 1st
p1 = Pbind(\instrument, \syn1,
     \freq, Pseq(s1*ffrq, ext),
	\dur, (1/s1.size)*1);
p2 = Pbind(\instrument, \syn1,
	\freq, Pseq(s1*ffrq, 1),
	\dur, (1/s1.size)*ext);

// 2nd
p3 = Pbind(\instrument, \syn1,
     \freq, Pseq(s2*ffrq, ext),
	\dur, (1/s1.size)*1);
p4 = Pbind(\instrument, \syn1,
	\freq, Pseq(s2*ffrq, 1),
	\dur, (1/s1.size)*ext);

// 3rd
p5 = Pbind(\instrument, \syn1,
	\freq, Pseq(s3*ffrq, ext),
	\dur, (1/s1.size)*1);
p6 = Pbind(\instrument, \syn1,
	\freq, Pseq(s3*ffrq, 1),
	\dur, (1/s1.size)*ext);

// 4th
p7 = Pbind(\instrument, \syn1,
     \freq, Pseq(s4*ffrq, ext),
	\dur, (1/s1.size)*1);
p8 = Pbind(\instrument, \syn1,
	\freq, Pseq(s4*ffrq, 1),
	\dur, (1/s1.size)*ext);

// 5th
p9 = Pbind(\instrument, \syn1,
	\freq, Pseq(s5*ffrq, ext),
	\dur, (1/s1.size)*1);
p10 = Pbind(\instrument, \syn1,
	\freq, Pseq(s5*ffrq, 1),
	\dur, (1/s1.size)*ext);

// 6th
p11 = Pbind(\instrument, \syn1,
	\freq, Pseq(s6*ffrq, ext),
	\dur, (1/s1.size)*1);
p12 = Pbind(\instrument, \syn1,
	\freq, Pseq(s6*ffrq, 1),
	\dur, (1/s1.size)*ext);

// 7th
p13 = Pbind(\instrument, \syn1,
	\freq, Pseq(s7*ffrq, ext),
	\dur, (1/s1.size)*1);
p14 = Pbind(\instrument, \syn1,
	\freq, Pseq(s7*ffrq, 1),
	\dur, (1/s1.size)*ext);

// 8th
p15 = Pbind(\instrument, \syn1,
	\freq, Pseq(s8*ffrq, ext),
	\dur, (1/s1.size)*1);
p16 = Pbind(\instrument, \syn1,
	\freq, Pseq(s8*ffrq, 1),
	\dur, (1/s1.size)*ext);

// Combining things in parallel
pp1 = Ppar([p1, p2]);
pp2 = Ppar([p3, p4]);
pp3 = Ppar([p5, p6]);
pp4 = Ppar([p7, p8]);
pp5 = Ppar([p9, p10]);
pp6 = Ppar([p11, p12]);
pp7 = Ppar([p13, p14]);
pp8 = Ppar([p15, p16]);

// Combining things in series
scr = [pp1, pp2, pp3, pp4, pp5, pp6, pp7, pp8];

pp = Pseq(scr, reps);

// render the pattern to wav
pp.render(
    path: odir +/+ "nrt_patterns3.wav"
	, maxTime: ext*reps*scr.size
	, headerFormat: "WAV"
);
