// more simple stuff
// read a soundfile
b = Buffer.read(s, "../../Users/ollie/Music/sounds/organ.wav");
b.numFrames;
// now play it
(
SynthDef(\playBuf0,{| out = 0, bufnum, startPos = 100000, dur = 10, amp = 1|
	var player, env;
	env = EnvGen.kr(Env.sine(dur, amp), doneAction: 2);
	player = PlayBuf.ar(1, bufnum, 1*BufRateScale.kr(bufnum), startPos: startPos);
    Out.ar(out, player*env)
}).play(s,[\bufnum, b]);
)

// Can we envelope it to stop it?
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

///////////
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
//////////


// mix buffer with other stuff
s.boot;

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

s.quit;

// mix two buffers
s.boot;


(
 var buf1, syn, pink;
// s.record;
 SynthDef(\playBuf1, {| out = 0, bufnum = 0, rate = 5, loop = 1 |
  var scaledRate, player;
  scaledRate = rate * BufRateScale.kr(bufnum);
  player = PlayBuf.ar(1, bufnum, scaledRate, loop: loop,
       doneAction:2);
  Out.ar(out, player)
 }).add;

 SynthDef(\playBufST, {| out = 0, bufnum = 0, rate = 1, loop = 1,
       dur = 1, amp = 0.2 |
  var scaledRate, player, env;

  env = EnvGen.kr(Env.sine(dur, amp), doneAction: 2);
  scaledRate = rate * BufRateScale.kr(bufnum);
  player = PlayBuf.ar(2, bufnum, scaledRate, loop:loop);
  Out.ar(out, player * env)
 }).add;

// buf1 = Buffer.read(s, "sounds/a11wlk01.wav");
buf1 = Buffer.read(s, "../../Users/ollie/Music/sounds/delila.wav");

 Task.new({

  s.sync; // wait for buf1
  syn = Synth(\playBuf1, [\out, 0, \bufnum, buf1.bufnum,
       \rate, 1, \loop, 1]);  // play buf1

  pink =  Buffer.read(s, "../../Users/ollie/Music/sounds/organ.wav",
   action: {  // run this when pink loads
    syn.set("loop", 0);
    syn = Synth(\playBufST, [\out, 0, \bufnum, pink.bufnum,
        \rate, 1, \loop, 1, \dur, 15]); // play pink
   });


 }).play
)

//////
File.getcwd