package yeelp.scalingfeast.integration.tic.conarm;

import java.util.Optional;

import c4.conarm.lib.book.content.ContentArmorModifier;
import slimeknights.mantle.client.book.BookTransformer;
import slimeknights.mantle.client.book.data.BookData;
import slimeknights.mantle.client.book.data.PageData;
import slimeknights.mantle.client.book.data.SectionData;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.book.content.ContentListing;
import slimeknights.tconstruct.library.modifiers.IModifier;

public final class ScalingFeastBookTransformer extends BookTransformer {

	@Override
	public void transform(BookData book) {
		SectionData sclfeastSec = null;
		SectionData modSec = null;
		
		for(SectionData section : book.sections) {
			if(section.name.equals("modifiers")) {
				modSec = section;
			}
			if(section.name.equals("scalingfeastmodifiers")) {
				sclfeastSec = section;
			}
		}
		if(modSec != null && sclfeastSec != null) {
			PageData firstPage = modSec.pages.get(0);
			Optional<ContentListing> listing = Optional.empty();
			if(firstPage.content instanceof ContentListing) {
				listing = Optional.of((ContentListing) firstPage.content);
			}
			for(PageData page : sclfeastSec.pages) {
				page.parent = modSec;
				modSec.pages.add(page);
				if(page.content instanceof ContentArmorModifier) {
					ContentArmorModifier content = (ContentArmorModifier) page.content;
					IModifier modifier = TinkerRegistry.getModifier(content.modifierName);
					if(modifier != null) {
						page.name = "scalingfeast_" + modifier.getIdentifier();
						listing.ifPresent((l) -> l.addEntry(modifier.getLocalizedName(), page));
					}
				}
			}
			sclfeastSec.pages.clear();
			book.sections.remove(sclfeastSec);
		}
	}
}
