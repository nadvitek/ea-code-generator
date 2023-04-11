import java.util.ArrayList;
import java.util.List;

public class Example {

class Partner {
	private List<OfferPartnerRelation> offers = new ArrayList<>();
}

class OfferPartnerRelation {
	private Partner partner;
	private Offer offer;
}

class Offer {
}

}
