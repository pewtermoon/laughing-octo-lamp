// more simple stuff
// read a soundfile
b = Buffer.read(s, "../../Users/ollie/Music/sounds/organ.wav");
b.numFrames;
// now play it
(
SynthDef(\playBuf0,{| out = 0, bufnum, startPos = 0, dur = 1, amp = 1, rate = 1|
	var scaledRate, player, env;
	env = EnvGen.kr(Env.perc(dur, amp), doneAction: 2);
	scaledRate = rate * BufRateScale.kr(bufnum);
	player = PlayBuf.ar(1, bufnum, scaledRate, startPos: startPos);
    Out.ar(out, player*env)
}).add; //play(s,[\bufnum, b]);
)

// what we want is a sequence of durs, startPoss, and bufratescales
(
var s1, s2, s3;
s.record(path: '../../Users/ollie/Music/sounds/organ_cycle_1.wav');
//s1 = [0.25,0.25,0.25,0.25, 0.125, 0.125, 0.25, 0.5, 1];
s1 = [2, 1, 0.5, 0.5, 0.25, 0.25, 0.25, 0.25, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125, 0.125];
s3 = [0.5, 0.6, 0.7, 0.8, 0.9, 1, 1.1, 1.2].reverse;
s2 = [100000, 150000, 200000, 250000, 100000, 100000, 230000, 500000, 110000];
Pseq([
Pbind(
	\instrument, \playBuf,
	\bufnum, Pfunc({b.bufnum}),
	\dur, Pseq(s1, 16),
	\amp, 1,
	\startPos, Pfunc({b.numFrames.rand}),
	\rate, Pseq(s3, 16)	//Pseq(s2, 8)
)], 1).play
)

p1 = Pbind(\instrument, \syn1,
     \freq, Pseq(s1*ffrq, ext),
	\dur, (1/s1.size)*1);
)
/////////
bufloaded = false;

 buf = Buffer.read(s, "sounds/a11wlk01.wav",
  action: { bufloaded = nil});

 Pseq([
  Pbind( // play this Pbind first
   \scale,  Scale.gong,
   \dur,   Pwhite(1, 3, inf) * 0.001,
   \degree,  Prand([0, 2, 4, \rest], inf),
   \amp,  0.2,
   \test,  Pfunc({ bufloaded }) // when this is nil, this Pbind ends
  ),
  Pbind( // next play this one
   \instrument, \playBuf,
   \bufnum,  Pfunc({buf.bufnum}), // use the buffer, now that we know it's ready
   \dur,  Pwhite(4, 4, 5),
   \startFrame, Pfunc({buf.numFrames.rand}),
   \amp,  1
  )
 ], 1). play
)

//////////////////////////
(
 var buf, bufloaded;

 SynthDef(\playBuf, {| out = 0, bufnum = 0, rate = 1,
       dur = 0.2, amp = 0.2, startPos = 0 |
  var scaledRate, player, env;

  env = EnvGen.kr(Env.sine(dur, amp), doneAction: 2);
  scaledRate = rate * BufRateScale.kr(bufnum);
  player = PlayBuf.ar(1, bufnum, scaledRate,
       startPos: startPos, loop:1);
  Out.ar(out, player * env)
 }).add;

 bufloaded = false;

 buf = Buffer.read(s, "sounds/a11wlk01.wav",
  action: { bufloaded = nil});

 Pseq([
  Pbind( // play this Pbind first
   \scale,  Scale.gong,
   \dur,   Pwhite(1, 3, inf) * 0.001,
   \degree,  Prand([0, 2, 4, \rest], inf),
   \amp,  0.2,
   \test,  Pfunc({ bufloaded }) // when this is nil, this Pbind ends
  ),
  Pbind( // next play this one
   \instrument, \playBuf,
   \bufnum,  Pfunc({buf.bufnum}), // use the buffer, now that we know it's ready
   \dur,  Pwhite(4, 4, 5),
   \startFrame, Pfunc({buf.numFrames.rand}),
   \amp,  1
  )
 ], 1). play
)
//////////////////////////

// ok, next need a way to PBind everything
\time, Pkey(\delta) / Pfunc { thisThread.clock.tempo },

(
b = Buffer.read(s, Platform.resourceDir +/+ "sounds/a11wlk01.wav");

SynthDef(\stretchedFragments, { |out, bufnum, start, time = 1, stretch = 1, amp = 1, attack = 0.01, decay = 0.05|
    var sig = PlayBuf.ar(1, bufnum, rate: stretch.reciprocal, startPos: start), eg;
    sig = PitchShift.ar(sig, pitchRatio: stretch);
    eg = EnvGen.kr(Env.linen(attack, time, decay), sig.abs > 0, doneAction: Done.freeSelf);
    Out.ar(out, (sig * eg) ! 2)
}).add; // note add! Without this, arguments won't work
)

(
TempoClock.default.tempo = 1;

p = Pbind(
    \instrument, \stretchedFragments,
    \bufnum, b,
    \start, Pwhite(0, (b.numFrames * 0.7).asInteger, inf),
    \delta, Pexprand(0.2, 1.5, inf),
    \time, Pkey(\delta),
    \stretch, Pexprand(1.0, 4.0, inf),
    \amp, 0.5,
    \attack, 0.1,
    \decay, 0.2
).play;
)

(
p = Pbind(
    \degree, Pseries(7, Pwhite(1, 3, inf) * Prand(#[-1, 1], inf), inf).fold(0, 14)
        + Prand(#[[-4, -8, -16], [-8, -7, -14], [-10, -9, -17], [-4, -8, -9]], inf),
    \dur, Pwrand(#[10, 1], #[1, 0.1], inf)
).play;
)