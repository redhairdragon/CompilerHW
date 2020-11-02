class A {
	public static void main(String[] a) {
	}
}

class B extends A {
	boolean a;

	public int a(int[] a) {
		return 1;
	}

	public boolean z() {
		return false;
	}
}

class C extends B {
	public A x() {
		B c;
		boolean m;
		C s;
		s = this;
		a = !(a && (this.z()));
		return new A();
	}

	public int a(int[] a) {
		return 1;
	}
}

class D extends B {
	public E m() {
		return new C();
	}
}

class E extends C {

}
