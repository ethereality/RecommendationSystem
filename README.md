# RecommendationSystem

This is a simple recommendation system based on collaborative filtering. That is, it uses similarity with other users to find recommended items. This was written to demonstrate how behavioural, test-driven development can be used to build software systems that are reliable and easy to maintain, by virtue of simple design and a robust test harness. You can find the accompanying tutorial at

The similarity between two users is determined by calculating their similarity index using a variation of the Jaccard Index. Given two users, U1 and U2, the similarity index between them is calculated as: S(U1, U2) = (|L1 ∩ L2|+|D1 ∩ D2|-|L1 ∩ D2|-|L2 ∩ D1|)/|L1 ∪ L2 ∪ D1 ∪ D2| , where L1 and L2 are the set of items liked by U1 and U2 respectively, and D1 and D2 are the set of items disliked by U1 and U2 respectively.

To find recommendations for a user, the probability of a user liking an item is calculated. Given a user U, and an item I, the probability that U will like I is: P(U, I) = (Z_L - Z_D)/(|I_L|+|I_D|), where Z_L and Z_D are, respectively, the similarities of U with other users who have liked I and other users who have disliked I, and I_L and I_D are, respectively, the total number of users who have liked I and the total number of users who have disliked I.

A detailed explanation of this method is given in Mahmud Ridwan's article on a simple collaborative filtering recommendation engine: https://www.toptal.com/algorithms/predicting-likes-inside-a-simple-recommendation-engine
