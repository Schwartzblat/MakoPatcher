"""
This module supposed to be edited by the user to generate the artifactory for their project.
"""
import json


def generate_artifactory(args):
    artifacts = dict()

    with open(args.artifactory, 'w') as file:
        json.dump(artifacts, file)
