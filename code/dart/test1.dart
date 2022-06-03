void main() {
  // 弱类型
  var num = 10;
  print(num);
  var str = "hello world";
  print(str);
  // 强类型
  int i1 = 124;
  double d1 = 123.1;
  String s1 = "hello world";
  bool b1 = true;
  List l1 = [1, 2, 3];
  List l2 = List.filled(0, 0, growable: true);
  l2.add(1);
  List l3 = const [1, 2, 3];
  print(l1);
  print(l2);
  print(l3);
  Map m1 = {"a": 1, "b": 2};
  print(m1);
}
