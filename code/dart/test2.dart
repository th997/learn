import 'dart:async';

void main() {
  print("start"); //1
  Timer.run(() {
    print("event"); // 4
    scheduleMicrotask(() {
      print("event microtask"); // 5
    });
  });
  scheduleMicrotask(() {
    print("microtask"); // 3
    Timer.run(() {
      print("microtask event"); //6
    });
  });
  print("end"); //2
}
