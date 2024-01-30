import gymnasium as gym
import numpy as np
import pickle

S = "4x4"  # map name corresponding with size

""" author: s12901 , Stanisław Kibort PJATK
    Usage of Reinforcement Learning with Gymnasium: FrozenLake-v1
    Robot makes action (0-3) and receives reward for successful state

    Code implements epsilon greedy algorithm -  common exploration strategy used in reinforcement learning.
    It balances exploration and exploitation to help agents learn more effectively
    it helps to discover rewards he otherwise would not """


def run(episodes, slippery, requestRender, is_training):
    """Initialize the environment.
    map_name: Name of the map that corresponds with its size
    is_slippery: Boolean indicating whether the robot is randomly slipping - making random action:
    move left(0), down(1), right(2), up(3)
    render_mode: Rendering mode to see map"""
    env = gym.make('FrozenLake-v1', map_name=S, is_slippery=slippery,
                   render_mode="human" if requestRender else None)
    if (is_training):
        q = np.zeros((env.observation_space.n, env.action_space.n))  # init 16x4 array
    else:
        f = open('training_dump_file.pkl', 'rb')  # get Q table from file
        q = pickle.load(f)
        f.close()

    # hyper parameters
    learning_rate_a = 0.9  # alpha of learning rate
    learning_discount_g = 0.9  # gamma or discount factor

    # epsilon greedy algorithm
    epsilon = 1  # if this factor is 1 then robot will take 100% random action
    epsilon_decay = 0.0001  # decay rate of epsilon , to make robot take less and less random actions
    random_number_generator = np.random.default_rng()

    success_counter = 0

    for i in range(episodes):  # with episodes number of times we want to train

        state = env.reset()[0]  # state between 0 and 15 that correspond with tile indexes, for example 0 is top left
        terminated = False  # terminates when robot falls into the ice hole
        truncated = False  # truncated is true when number of actions exceeds 200

        while not truncated and not terminated:
            if is_training and random_number_generator.random() < epsilon:
                action = env.action_space.sample()  # random action
            else:
                action = np.argmax(q[state, :])  # action based on Q table
            new_state, reward, terminated, truncated, _ = env.step(action)
            if is_training:
                """Q-Learning formula that rewards good state-action pair (updates q[state,action]) with every good step made"""
            q[state, action] = q[state, action] + learning_rate_a * (
                    reward + learning_discount_g * np.max(q[new_state, :]) - q[state, action]
            )

            state = new_state
        epsilon = max(epsilon - epsilon_decay, 0)  # decrease epsilon by epsilon decay rate
        if epsilon == 0:
            learning_rate_a = 0.0001
        if reward == 1:
            success_counter = success_counter + 1
    env.close()
    print("Number of success rewards gathered: ", success_counter)

    if is_training:
        f = open("training_dump_file.pkl", "wb")
        pickle.dump(q, f)
        f.close()


"""Main function"""
if __name__ == "__main__":
    run(10000, True, True, False)
