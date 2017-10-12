
public class Test {
public static void main(String a[]){
	String path="F:/dimen/values/dimens.xml";
	DimenCreate dimenCreate=new DimenCreate(path);
	
	for(int i=360;i<=640;i=i+10){
		dimenCreate.setDP(360, i);
		dimenCreate.getDimenFile();
	}

	
}

}
